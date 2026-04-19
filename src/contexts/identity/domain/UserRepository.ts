import { User } from './User'

export interface UserRepository {
  save: (user: User) => Promise<User>
  findById: (id: string) => Promise<User>
  findByEmail: (email: string) => Promise<User | null>
}
