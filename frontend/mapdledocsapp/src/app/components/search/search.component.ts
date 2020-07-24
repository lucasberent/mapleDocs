import {Component, OnInit, ViewChild} from '@angular/core';
import {SearchService} from '../../service/search.service';
import {Router} from "@angular/router";
import {MaDmpDisplayObject} from "../../dto/madmp-display-object";
import {ToastrService} from "ngx-toastr";
import {SearchResponse} from "../../dto/search-response";
import {PageEvent} from "@angular/material/paginator";
import {MatTabGroup} from "@angular/material/tabs";
import {SearchDTO} from "../../dto/search-dto";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  madmpDisplayList: MaDmpDisplayObject[] = [];
  searchBoxValue: string;
  fieldInputValue: string;
  valueInputValue: string;

  ethicalIssuesValue: string;
  embargoValue: string;

  creationFromDate: Date;
  creationToDate: Date;
  modificationFromDate: Date;
  modificationToDate: Date;

  pageEvent: PageEvent;

  length: number = 0;
  pageSize: number = 10;
  pageSizeOptions: number[] = [10, 25, 50, 100];

  currentPage: number = 0;
  currentPageSize: number = this.pageSize;
  noSelectionString: string = 'no-selection';
  contactIdTypes: string[] = [this.noSelectionString, 'orcid', 'insi', 'openid', 'other'];
  contactPersonName: string;
  contactPersonEmail: string;
  contactPersonIdentifier: string;
  contactPersonIdentifierType: string = this.noSelectionString;

  @ViewChild('tabGroup') tabGroup: MatTabGroup;
  datasetIdentifier: string;
  datasetIdentifierType: string;
  datasetIdTypes: string[] = [this.noSelectionString, 'handle', 'doi', 'ark', 'url', 'other'];
  datasetDistributionHostUrl: string;
  metadataStandardId: string;
  metadataStandardIdTypes: string[] = [this.noSelectionString, 'url', 'other'];
  metadataStandardIdType: string;

  constructor(private searchService: SearchService, private router: Router, private toastrService: ToastrService) {
  }

  ngOnInit(): void {
  }

  private setDisplayMadmps(madmps: SearchResponse<any>) {
    this.madmpDisplayList = [];
    if (!madmps.hits || !madmps.hits.hits) {
      return;
    }
    madmps.hits.hits.forEach(searchResult => {
      const docId = searchResult._source['mongoId'];
      const madmpDisplay = new MaDmpDisplayObject(searchResult._source, docId);
      console.log(madmpDisplay);
      this.madmpDisplayList.push(madmpDisplay);
    });
  }

  onSelectedTabChange() {
    this.setDisplayMadmps(<any>[]);
    this.length = 0;
  }

  doSearch() {
    switch (this.tabGroup.selectedIndex) {
      case 0:
        this.doSearchFullText();
        break;
      case 1:
        this.doSearchCombined();
        break;
      case 2:
        this.doSearchCustom();
        break;
    }
  }

  doSearchFullText() {
    const value = this.searchBoxValue;
    if (value.length >= 4 || value.length === 0) {
      console.log('searching for: ' + value);
      this.searchService.findMaDmps(value, this.currentPage, this.currentPageSize).subscribe(madmps => {
        this.setDisplayMadmps(madmps);
        this.length = madmps.hits.total.value;
      });
    }
  }

  doSearchCustom() {
    const field = this.fieldInputValue;
    const value = this.valueInputValue;
    if (field.length >= 4 && value.length >= 4) {
      console.log('searching for: ' + field + ' = ' + value);
      this.searchService.findMaDmpsCustomField(field, value, this.currentPage, this.currentPageSize)
        .subscribe(madmps => {
          this.setDisplayMadmps(madmps);
          this.length = madmps.hits.total.value;
        });
    }
  }

  doSearchCombined() {
    const searchDTO = this.buildSearchDTO();
    this.searchService
      .findMaDmpsCombined(searchDTO)
      .subscribe(madmps => {
        this.setDisplayMadmps(madmps);
        this.length = madmps.hits.total.value;
      });
  }

  onToUpload() {
    console.log('to upload');
    this.router.navigate(['/upload']);
  }

  pageChanged(event?: PageEvent) {
    this.currentPage = event.pageIndex;
    this.currentPageSize = event.pageSize;
    this.doSearch();
  }

  onContactIdTypeSelectionChange(event) {
    if (event.value === this.noSelectionString) {
      this.contactPersonIdentifierType = null;
    } else {
      this.contactPersonIdentifierType = event.value;
    }
    this.doSearch();
  }

  onDatasetIdTypeSelectionChange(event) {
    if (event.value === this.noSelectionString) {
      this.datasetIdentifierType = null;
    } else {
      this.datasetIdentifierType = event.value;
    }
    this.doSearch();
  }

  private buildSearchDTO(): SearchDTO {
    this.prepareSelectionboxFields();
    return new SearchDTO(
      this.datasetIdentifier,
      this.datasetIdentifierType,
      this.contactPersonName,
      this.contactPersonEmail,
      this.contactPersonIdentifier,
      this.contactPersonIdentifierType,
      this.ethicalIssuesValue,
      this.embargoValue,
      this.creationFromDate,
      this.creationToDate,
      this.modificationFromDate,
      this.modificationToDate,
      this.currentPage,
      this.currentPageSize,
      this.datasetDistributionHostUrl,
      this.metadataStandardId,
      this.metadataStandardIdType);
  }

  prepareSelectionboxFields() {
    if (this.contactPersonIdentifierType === this.noSelectionString) {
      this.contactPersonIdentifierType = null;
    }
    if (this.datasetIdentifierType === this.noSelectionString) {
      this.datasetIdentifierType = null;
    }
    if (this.metadataStandardIdType === this.noSelectionString) {
      this.metadataStandardIdType = null;
    }
  }

  onMetadataStandardIdTypeChange(event) {
    if (event.value === this.noSelectionString) {
      this.metadataStandardIdType = null;
    } else {
      this.metadataStandardIdType = event.value;
    }
    this.doSearch();
  }
}
