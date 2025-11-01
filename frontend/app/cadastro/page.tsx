'use client';

import { useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Link2, UserPlus } from 'lucide-react';
import { cadastrarUsuario } from '@/lib/api';
import Button from '@/components/Button';
import Input from '@/components/Input';
import Card from '@/components/Card';

export default function CadastroPage() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    nomeUsuario: '',
    email: '',
    senha: '',
    confirmarSenha: '',
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

    if (formData.senha !== formData.confirmarSenha) {
      setError('As senhas não coincidem');
      return;
    }

    if (formData.senha.length < 6) {
      setError('A senha deve ter no mínimo 6 caracteres');
      return;
    }

    setLoading(true);

    try {
      await cadastrarUsuario({
        nomeUsuario: formData.nomeUsuario,
        email: formData.email,
        senha: formData.senha,
      });
      
      alert('Cadastro realizado com sucesso! Faça login para continuar.');
      router.push('/login');
    } catch (err: any) {
      console.error('Erro ao cadastrar:', err);
      
      if (err.response?.data) {
        const errorData = err.response.data;
        const errorMsg = errorData.nomeUsuario || 
                         errorData.email || 
                         errorData.senha ||
                         errorData.message ||
                         'Erro ao cadastrar usuário';
        setError(errorMsg);
      } else {
        setError('Erro ao conectar com o servidor. Verifique se o backend está rodando.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
      <Card className="w-full max-w-md">
        <div className="text-center mb-6">
          <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <UserPlus className="w-8 h-8 text-blue-600" />
          </div>
          <h1 className="text-2xl font-bold text-gray-900 mb-2">Criar Conta</h1>
          <p className="text-gray-600">Preencha os dados para se cadastrar</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="nomeUsuario" className="block text-sm font-medium text-gray-700 mb-2">
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
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
              Email
            </label>
            <Input
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="seu@email.com"
              required
              disabled={loading}
            />
          </div>

          <div>
            <label htmlFor="senha" className="block text-sm font-medium text-gray-700 mb-2">
              Senha
            </label>
            <Input
              id="senha"
              name="senha"
              type="password"
              value={formData.senha}
              onChange={handleChange}
              placeholder="Mínimo 6 caracteres"
              required
              disabled={loading}
            />
          </div>

          <div>
            <label htmlFor="confirmarSenha" className="block text-sm font-medium text-gray-700 mb-2">
              Confirmar Senha
            </label>
            <Input
              id="confirmarSenha"
              name="confirmarSenha"
              type="password"
              value={formData.confirmarSenha}
              onChange={handleChange}
              placeholder="Digite a senha novamente"
              required
              disabled={loading}
            />
          </div>

          {error && (
            <div className="p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
              {error}
            </div>
          )}

          <Button type="submit" disabled={loading} className="w-full">
            {loading ? 'Cadastrando...' : 'Cadastrar'}
          </Button>

          <div className="text-center text-sm text-gray-600">
            Já tem uma conta?{' '}
            <Link href="/login" className="text-blue-600 hover:text-blue-700 font-medium">
              Fazer login
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
