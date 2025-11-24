# ===== BASE =====
FROM node:20-alpine AS base
WORKDIR /app

# copie os package.json da pasta frontend
COPY ./frontend/package*.json ./

RUN npm install

# ===== BUILD =====
FROM base AS build

# copie todo o frontend
COPY ./frontend .

RUN npm run build

# ===== RUN =====
FROM node:20-alpine AS production
WORKDIR /app

ENV NODE_ENV=production

COPY --from=build /app ./

EXPOSE 3000
CMD ["npm", "start"]
