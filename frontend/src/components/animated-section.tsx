"use client";
import { useEffect, useState, ReactNode } from "react";
import { motion } from "framer-motion";

export function AnimatedSection({ children }: { children: ReactNode }) {
  const [scrollY, setScrollY] = useState(0);

  useEffect(() => {
    const handleScroll = () => {
      setScrollY(window.scrollY);
    };
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const fadeInUp = {
    hidden: { opacity: 0, y: 20 },
    visible: {
      opacity: 1,
      y: 0,
      transition: { duration: 0.6 },
    },
  };

  const staggerContainer = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.2,
      },
    },
  };

  // childrenにはmotion.divやvariantsなどを使った構造を渡すことを想定
  return (
    <motion.div
      className="max-w-4xl mx-auto"
      initial="hidden"
      whileInView="visible"
      viewport={{ once: true, margin: "-100px" }}
      variants={fadeInUp}
    >
      {typeof children === "function"
        ? children({ scrollY, fadeInUp, staggerContainer })
        : children}
    </motion.div>
  );
} 