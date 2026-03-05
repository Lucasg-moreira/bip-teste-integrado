# Benefícios App — Angular 17

Aplicação Angular para gerenciamento de benefícios com CRUD e transferência de saldo.

## Pré-requisitos

- Node.js 18+
- Angular CLI 17+: `npm install -g @angular/cli`

## Setup

```bash
# 1. Instalar dependências
npm install

# 2. Iniciar servidor de desenvolvimento
ng serve

# Acesse em: http://localhost:4200
```

## Configuração da API

Por padrão, a app aponta para `http://localhost:8081/api/v1/beneficios`.

Para alterar, edite o arquivo:
```
src/app/services/beneficio.service.ts
```

Mude a propriedade `baseUrl`:
```ts
private readonly baseUrl = 'http://SEU_BACKEND/api/v1/beneficios';
```

## Funcionalidades

| Funcionalidade | Endpoint | Método |
|---|---|---|
| Listar benefícios | `/api/v1/beneficios` | GET |
| Criar benefício | `/api/v1/beneficios` | POST |
| Atualizar benefício | `/api/v1/beneficios/{id}` | PUT |
| Transferir saldo | `/api/v1/beneficios/transferencia` | POST |

## Estrutura do Projeto

```
src/
├── app/
│   ├── components/
│   │   ├── beneficio-list/        # Tela principal com tabela
│   │   ├── beneficio-form/        # Modal de criar/editar
│   │   ├── transferencia/         # Modal de transferência
│   │   └── shared/
│   │       └── toast-container/   # Notificações
│   ├── models/
│   │   └── beneficio.model.ts     # Interfaces/DTOs
│   ├── services/
│   │   ├── beneficio.service.ts   # Chamadas HTTP
│   │   └── toast.service.ts       # Notificações
│   ├── app.component.ts
│   ├── app.config.ts
│   └── app.routes.ts
└── styles.scss                    # Estilos globais
```

## Build para produção

```bash
ng build --configuration production
```

Os arquivos estarão em `dist/beneficios-app/`.
