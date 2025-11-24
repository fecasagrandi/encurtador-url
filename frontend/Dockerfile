# ===== BASE =====
FROM node:20-alpine AS base
WORKDIR /app
COPY package*.json ./
RUN npm install

# ===== BUILD =====
FROM base AS build
COPY . .
RUN npm run build

# ===== RUN =====
FROM base AS production
ENV NODE_ENV=production
COPY --from=build /app/.next ./.next
COPY --from=build /app/public ./public
COPY --from=build /app/node_modules ./node_modules
COPY --from=build /app/package*.json ./
EXPOSE 3000
CMD ["npm", "start"]
