'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Link2, LogIn } from 'lucide-react';
import { loginUsuario } from '@/lib/api';
import Button from '@/components/Button';
import Input from '@/components/Input';
import Card from '@/components/Card';
import ThemeToggle from '@/components/ThemeToggle';

export default function LoginPage() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    nomeUsuario: '',
    senha: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const usuario = await loginUsuario({
        nomeUsuario: formData.nomeUsuario,
        senha: formData.senha,
      });
      
      localStorage.setItem('usuario', JSON.stringify(usuario));
      localStorage.setItem('credentials', btoa(`${formData.nomeUsuario}:${formData.senha}`));
      
      router.push('/admin');
    } catch (err: any) {
      setError('Usuário ou senha inválidos');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 dark:from-gray-900 dark:to-gray-800 flex items-center justify-center p-4">
      <div className="absolute top-4 right-4">
        <ThemeToggle />
      </div>
      <Card className="w-full max-w-md">
        <div className="text-center mb-6">
          <div className="w-16 h-16 bg-blue-100 dark:bg-blue-900 rounded-full flex items-center justify-center mx-auto mb-4">
            <LogIn className="w-8 h-8 text-blue-600 dark:text-blue-400" />
          </div>
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-2">Entrar</h1>
          <p className="text-gray-600 dark:text-gray-400">Acesse sua conta</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="nomeUsuario" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              Nome de Usuário
            </label>
            <Input
              id="nomeUsuario"
              name="nomeUsuario"
              type="text"
              value={formData.nomeUsuario}
              onChange={handleChange}
              placeholder="Digite seu nome de usuário"
              required
              disabled={loading}
            />
          </div>

          <div>
            <label htmlFor="senha" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              Senha
            </label>
            <Input
              id="senha"
              name="senha"
              type="password"
              value={formData.senha}
              onChange={handleChange}
              placeholder="Digite sua senha"
              required
              disabled={loading}
            />
          </div>

          {error && (
            <div className="p-3 bg-red-50 dark:bg-red-900 border border-red-200 dark:border-red-700 rounded-lg text-red-700 dark:text-red-200 text-sm">
              {error}
            </div>
          )}

          <Button type="submit" disabled={loading} className="w-full">
            {loading ? 'Entrando...' : 'Entrar'}
          </Button>

          <div className="text-center text-sm text-gray-600 dark:text-gray-400">
            Não tem uma conta?{' '}
            <Link href="/cadastro" className="text-blue-600 dark:text-blue-400 hover:text-blue-700 dark:hover:text-blue-300 font-medium">
              Cadastre-se
            </Link>
          </div>

          <Link href="/">
            <Button variant="secondary" className="w-full">
              Voltar para Home
            </Button>
          </Link>
        </form>
      </Card>
    </div>
  );
}
