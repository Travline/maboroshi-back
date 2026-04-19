export class UserMail {
  private readonly emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

  constructor (private readonly mail: string) {
    if (!this.isValid(mail)) {
      throw new Error(`Invalid email format: ${mail}`)
    }
  }

  private isValid (mail: string): boolean {
    return this.emailRegex.test(mail)
  }

  public toString (): string {
    return this.mail
  }

  public equals (other: UserMail): boolean {
    return other.toString() === this.mail
  }
}
