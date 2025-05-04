"use client"

import type React from "react"

import { useState, useRef, useEffect } from "react"
import { Bot, ArrowUp, User, Loader2, Zap } from "lucide-react"
import { Button } from "@/components/ui/button"
import { motion } from "framer-motion"
import Link from "next/link"
import { Switch } from "@/components/ui/switch"

type Message = {
  id: string
  content: string
  isUser: boolean
}

export default function ChatPage() {
  const [messages, setMessages] = useState<Message[]>([
    {
      id: "1",
      content: "Hi there! I'm K-AI, your AI assistant. How can I help you today?",
      isUser: false,
    },
  ])
  const [input, setInput] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const [streamEnabled, setStreamEnabled] = useState(false)
  const messagesEndRef = useRef<HTMLDivElement>(null)
  const inputRef = useRef<HTMLInputElement>(null)

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" })
  }

  useEffect(() => {
    scrollToBottom()
    inputRef.current?.focus()
  }, [messages])

  const handleSendMessage = async () => {
    if (!input.trim() || isLoading) return

    // Add user message
    const userMessage: Message = {
      id: Date.now().toString(),
      content: input,
      isUser: true,
    }
    setMessages((prev) => [...prev, userMessage])

    const userInput = input
    setInput("")
    setIsLoading(true)

    try {
      if (!streamEnabled) {
        // 使用普通接口
        const response = await fetch("http://localhost:8080/api/conversation/chat", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            message: userInput,
            provider: "siliconflow",
          }),
        })

        if (!response.ok) {
          throw new Error(`API request failed with status ${response.status}`)
        }

        const data = await response.json()

        // Add AI response with the data from the API
        const aiMessage: Message = {
          id: (Date.now() + 1).toString(),
          content: data.data.content || JSON.stringify(data),
          isUser: false,
        }
        setMessages((prev) => [...prev, aiMessage])
      } else {
        // 使用流式接口
        const aiMessageId = (Date.now() + 1).toString()
        
        // 先添加一个空的AI消息
        const initialAiMessage: Message = {
          id: aiMessageId,
          content: "",
          isUser: false,
        }
        
        setMessages((prev) => [...prev, initialAiMessage])
        
        const response = await fetch("http://localhost:8080/api/conversation/stream-chat", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            message: userInput,
            provider: "siliconflow",
          }),
        })

        if (!response.ok) {
          throw new Error(`API request failed with status ${response.status}`)
        }

        if (!response.body) {
          throw new Error("Response body is null")
        }

        // 处理流式响应
        const reader = response.body.getReader()
        const decoder = new TextDecoder()
        
        let accumulatedContent = ""
        
        try {
          while (true) {
            const { done, value } = await reader.read()
            if (done) break
            
            const text = decoder.decode(value)
            
            // 处理返回的数据行
            const lines = text.split('\n').filter(line => line.trim().startsWith('data:'))
            
            for (const line of lines) {
              try {
                // 提取data:后面的JSON字符串并解析
                const jsonStr = line.substring(5).trim()
                if (!jsonStr) continue
                
                const data = JSON.parse(jsonStr)
                
                // 提取content
                const content = data.choices?.[0]?.delta?.content || ''
                if (content) {
                  accumulatedContent += content
                  
                  // 更新消息内容，实现打字机效果
                  setMessages((prev) => 
                    prev.map((msg) => 
                      msg.id === aiMessageId 
                        ? { ...msg, content: accumulatedContent } 
                        : msg
                    )
                  )
                }
              } catch (error) {
                console.error("Error parsing JSON:", error, line)
              }
            }
          }
        } catch (error) {
          console.error("Error reading stream:", error)
          throw error
        }
      }
    } catch (error) {
      console.error("Error sending message:", error)

      // Add error message
      const errorMessage: Message = {
        id: (Date.now() + 1).toString(),
        content: `Sorry, there was an error processing your request. Please try again later. (Error: ${error instanceof Error ? error.message : "Unknown error"})`,
        isUser: false,
      }
      setMessages((prev) => [...prev, errorMessage])
    } finally {
      setIsLoading(false)
    }
  }

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault()
      handleSendMessage()
    }
  }

  return (
    <div className="flex flex-col h-screen bg-white">
      {/* Header */}
      <header className="flex items-center justify-between px-6 py-4 border-b border-gray-200">
        <Link href="/" className="flex items-center space-x-2">
          <Bot className="w-6 h-6 text-purple-500" />
          <span className="font-medium text-black">K-AI Chat</span>
        </Link>
        <div className="flex items-center space-x-4">
          <div className="flex items-center space-x-2">
            <Zap className={`w-4 h-4 ${streamEnabled ? "text-purple-500" : "text-gray-400"}`} />
            <span className="text-sm text-gray-600">流式传输</span>
            <Switch 
              checked={streamEnabled}
              onCheckedChange={setStreamEnabled}
              className="data-[state=checked]:bg-purple-500 rounded-full"
            />
          </div>
          <Button variant="ghost" size="sm" className="text-gray-500 rounded-xl hover:bg-purple-300 hover:text-black">
            New Chat
          </Button>
        </div>
      </header>

      {/* Chat messages */}
      <div className="flex-1 overflow-y-auto p-4 space-y-4">
        {messages.map((message) => (
          <motion.div
            key={message.id}
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            className={`flex ${message.isUser ? "justify-end" : "justify-start"}`}
          >
            <div
              className={`max-w-[80%] md:max-w-[70%] rounded-2xl px-4 py-3 ${
                message.isUser ? "bg-white border border-gray-200 text-black" : "bg-purple-50 text-gray-800"
              }`}
            >
              <div className="flex items-start gap-2">
                {!message.isUser && <Bot className="w-5 h-5 text-purple-500 mt-1" />}
                <div className="flex-1 whitespace-pre-wrap">{message.content}</div>
                {message.isUser && <User className="w-5 h-5 text-gray-400 mt-1" />}
              </div>
            </div>
          </motion.div>
        ))}

        {/* Loading indicator */}
        {isLoading && (
          <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="flex justify-start">
            <div className="bg-purple-50 rounded-2xl px-4 py-3 text-gray-800">
              <div className="flex items-center gap-2">
                <Bot className="w-5 h-5 text-purple-500" />
                <Loader2 className="w-4 h-4 animate-spin text-purple-500" />
                <span>Thinking...</span>
              </div>
            </div>
          </motion.div>
        )}

        <div ref={messagesEndRef} />
      </div>

      {/* Input area */}
      <div className="border-t border-gray-200 p-4">
        <div className="max-w-4xl mx-auto relative">
          <input
            ref={inputRef}
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder="Enter a message"
            disabled={isLoading}
            className="w-full px-4 py-3 pr-12 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent"
          />
          <Button
            onClick={handleSendMessage}
            disabled={!input.trim() || isLoading}
            className="absolute right-2 top-1/2 transform -translate-y-1/2 bg-purple-500 hover:bg-purple-600 text-white rounded-full p-2 h-auto"
          >
            {isLoading ? <Loader2 className="w-4 h-4 animate-spin" /> : <ArrowUp className="w-4 h-4" />}
          </Button>
        </div>
      </div>
    </div>
  )
}
