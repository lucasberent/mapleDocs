export class RegisterDto {
  constructor(
    public login: string,
    public password: string,
    public doiServiceUsername: string,
    public doiServiceDoiPrefix: string
  ) {
  }
}
