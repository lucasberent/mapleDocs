export class SearchDTO {
  constructor(
    public datasetIdentifier: string,
    public datasetIdentifierType: string,
    public contactPersonName: string,
    public contactPersonEmail: string,
    public contactPersonIdentifier: string,
    public contactPersonIdentifierType: string,
    public ethicalIssues: string,
    public embargo: string,
    public creationFromDate: Date,
    public creationToDate: Date,
    public modificationFromDate: Date,
    public modificationToDate: Date,
    public page: number,
    public size: number,
    public datasetDistributionHostUrl: string,
    public metadataStandardId: string,
    public metadataStandardIdType: string
  ) {
  }
}
