"use client"

import { Button } from "@/components/ui/button"
import { Bot, Menu, Github } from "lucide-react"
import { motion } from "framer-motion"
import Link from "next/link"
import type React from "react"

export default function Navbar() {
  return (
    <motion.nav
      initial={{ y: -100 }}
      animate={{ y: 0 }}
      className="flex items-center justify-between px-6 py-4 backdrop-blur-sm border-b border-black/10"
    >
      <Link href="/" className="flex items-center space-x-2">
        <Bot className="w-8 h-8 text-purple-500" />
        <span className="text-black font-medium text-xl">K-AI</span>
      </Link>

      {/* Removing the NavLinks */}

      <div className="hidden md:flex items-center space-x-4">
      <Button variant="ghost" size="icon" className="text-black bg-white hover:text-purple-500 hover:bg-white">
  <Github className="w-5 h-5" />
</Button>
        <Link href="/chat">
          <Button className="bg-purple-600 hover:bg-purple-700 text-white rounded-xl">Get Started</Button>
        </Link>
      </div>

      <Button variant="ghost" size="icon" className="md:hidden text-black">
        <Menu className="w-6 h-6" />
      </Button>
    </motion.nav>
  )
}

function NavLink({ href, children }: { href: string; children: React.ReactNode }) {
  return (
    <Link href={href} className="text-gray-700 hover:text-black transition-colors relative group">
      {children}
      <span className="absolute -bottom-1 left-0 w-0 h-0.5 bg-purple-500 transition-all group-hover:w-full" />
    </Link>
  )
}
