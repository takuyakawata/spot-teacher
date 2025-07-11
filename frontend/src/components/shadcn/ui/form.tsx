import * as React from "react"
import { Slot } from "@radix-ui/react-slot"
import {
  Controller,
  FormProvider,
  useFormContext,
  type FieldValues,
  type UseFormReturn,
} from "react-hook-form"

interface FormProps<T extends FieldValues = FieldValues> {
  children: React.ReactNode
  form: UseFormReturn<T>
  onSubmit: (e: any) => void
  className?: string
}

export function Form<T extends FieldValues = FieldValues>({ children, form, onSubmit, className }: FormProps<T>) {
  return (
    <FormProvider {...form} >
      <form onSubmit={(e) => { e.preventDefault(); form.handleSubmit(onSubmit)(); }} className={className}>
        {children}
      </form>
    </FormProvider>
  )
}

export function FormField({ name, children }: { name: string; children: (props: { field: any; fieldState: any }) => React.ReactNode }) {
  const { control } = useFormContext()
  return <Controller name={name} control={control} render={({ field, fieldState }) => children({ field, fieldState })} />
}

export function FormItem({ children, className }: { children: React.ReactNode; className?: string }) {
  return <div className={className}>{children}</div>
}

export function FormLabel({ children, className }: { children: React.ReactNode; className?: string }) {
  return <label className={"text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70" + (className ? ` ${className}` : "")}>{children}</label>
}

export function FormControl({ children }: { children: React.ReactNode }) {
  return <Slot>{children}</Slot>
}

export function FormMessage({ children, className }: { children?: React.ReactNode; className?: string }) {
  if (!children) return null
  return <p className={"text-xs text-red-500 mt-1" + (className ? ` ${className}` : "")}>{children}</p>
} 