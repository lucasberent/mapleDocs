export class CreateMaDmpDto {
  constructor(
    public json: string,
    public fieldsToHide: string[]
  ) {
  }
}
