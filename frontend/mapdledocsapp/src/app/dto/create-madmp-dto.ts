export class CreateMaDmpDto {
  constructor(
    public json: string,
    public fieldsToHide: string[],
    public assignNewDoi: boolean,
    public doiServicePassword: string
  ) {
  }
}
