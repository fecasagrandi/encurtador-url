import { HTMLAttributes } from 'react';
import { cn } from '@/lib/utils';

interface CardProps extends HTMLAttributes<HTMLDivElement> {}

export default function Card({ className, children, ...props }: CardProps) {
  return (
    <div
      className={cn('bg-white rounded-lg shadow-md p-6', className)}
      {...props}
    >
      {children}
    </div>
  );
}
