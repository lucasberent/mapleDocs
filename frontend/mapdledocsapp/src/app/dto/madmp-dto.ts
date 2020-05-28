export class MaDmpDto {
  constructor(
    public json: string,
    public docId: number,
    public userId: number,
    public fieldsToHide: string[]
  ) {
  }
}
