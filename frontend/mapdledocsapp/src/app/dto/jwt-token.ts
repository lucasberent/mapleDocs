export class JwtToken {
  constructor(
    public payload: string,
    public login: string
  ) {}
}
