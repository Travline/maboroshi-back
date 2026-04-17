import express from 'express'
import cors from 'cors'
import cookieParser from 'cookie-parser'

const app = express()

app.use(cors({
  origin: [
    'http://localhost:5173'
  ],
  credentials: true
}))

app.use(express.json())
app.use(cookieParser())
app.set('trust proxy', true)

app.use('/oal', (_req, res) => {
  res.send('oal')
})

const PORT = process.env.PORT ?? 3000
app.listen(PORT, () => {
  console.log('Starting server')
})
