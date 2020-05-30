import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {SearchService} from '../../service/search.service';
import {Router} from "@angular/router";
import {MaDmpDto} from "../../dto/madmp-dto";
import {MaDmpDisplayObject} from "../../dto/madmp-display-object";
import {newArray} from "@angular/compiler/src/util";
import {ToastrService} from "ngx-toastr";
import {SearchResponse} from "../../dto/search-response";
import {PageEvent} from "@angular/material/paginator";
import {MatTabGroup} from "@angular/material/tabs";

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

  ethicalIssuesValue;
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

  @ViewChild('tabGroup') tabGroup: MatTabGroup;

  constructor(private searchService: SearchService, private router: Router, private toastrService:ToastrService) {
  }

  ngOnInit(): void {
  }

  private setDisplayMadmps(madmps: SearchResponse<any>) {
    this.madmpDisplayList = [];
    madmps.hits.hits.forEach(searchResult => {
      const docId = searchResult._source['mongo_id'];
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
    this.searchService.findMaDmpsCombined(this.ethicalIssuesValue, this.creationFromDate, this.creationToDate,
      this.modificationFromDate, this.modificationToDate, this.currentPage, this.currentPageSize)
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
}
