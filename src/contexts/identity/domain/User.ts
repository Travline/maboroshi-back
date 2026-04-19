import { v7 as uuidv7 } from 'uuid'

export class User {
  constructor (
    private readonly username: string,
    private readonly mail: string, // Queda pendiente pasarlo a un Value Object
    private readonly pwd: string,
    private readonly phone: number,
    private readonly id: string = uuidv7(),
    private isActive: boolean
  ) {}

  public deactivate (): void {
    this.isActive = false
  }

  public toPrimitives (): Record<string, string | number> {
    return {
      id: this.id,
      username: this.username,
      mail: this.mail,
      phone: this.phone
    }
  }

  public getPwd (): string {
    return this.pwd
  }

  public getIsActive (): boolean {
    return this.isActive
  }
}
