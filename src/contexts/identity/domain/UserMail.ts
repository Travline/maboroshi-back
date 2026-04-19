export class UserMail {
  constructor (
    private readonly mail: string
  ) {
    if (!mail.includes('@')) {
      throw new Error('Mail value invalid')
    }
  }

  public toString (): string {
    return this.mail
  }

  public equals (newMail: UserMail): boolean {
    return newMail.toString() === this.mail
  }
}
