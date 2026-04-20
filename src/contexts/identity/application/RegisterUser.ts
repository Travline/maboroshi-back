import type { UserRepository } from '../domain/UserRepository'
import { User } from '../domain/User'
import type { PasswordHasher } from '../domain/PasswordHasher'
import type { RegisterUserRequest } from './RegisterUserRequest'
import { UserMail } from '../domain/UserMail'

export class RegisterUser {
  constructor (
    private readonly userRepository: UserRepository,
    private readonly passwordHasher: PasswordHasher
  ) {}

  public async execute (userRequest: RegisterUserRequest): Promise<void> {
    if (userRequest.pwd.length < 8) {
      throw new Error('Password too short')
    }

    const existsUser = await this.userRepository.findByEmail(userRequest.mail)
    if (existsUser !== null) {
      throw new Error('User already exists')
    }

    const pwdHashed = await this.passwordHasher.hash(userRequest.pwd)

    const user = new User(
      userRequest.username,
      new UserMail(userRequest.mail),
      pwdHashed,
      userRequest.phone,
      undefined,
      true
    )

    await this.userRepository.save(user)
  }
}
