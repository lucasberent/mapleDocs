import {Component, OnInit} from '@angular/core';
import {SearchService} from '../../service/search.service';
import {Router} from "@angular/router";
import {MaDmpDto} from "../../dto/madmp-dto";
import {MaDmpDisplayObject} from "../../dto/madmp-display-object";
import {newArray} from "@angular/compiler/src/util";
import {ToastrService} from "ngx-toastr";
import {SearchResponse} from "../../dto/search-response";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  madmpDisplayList: MaDmpDisplayObject[] = [];

  constructor(private searchService: SearchService, private router: Router, private toastrService:ToastrService) {
  }

  ngOnInit(): void {
    this.searchService.findMaDmps('', 1, 10).subscribe(madmps => {
      if (madmps.hits.total === 0){
        this.toastrService.info('no madmps for search criteria found');
      }
      this.setDisplayMadmps(madmps);
    }); // TODO pagination
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

  onEnter(value: string) {
    this.doSearch(value);
  }

  doSearch(value: string) {
    if (value.length >= 4 || value.length === 0) {
      console.log('searching for: ' + value);
      this.searchService.findMaDmps(value, 1, 10).subscribe(madmps => this.setDisplayMadmps(madmps));
    }
  }

  onToUpload() {
    console.log('to upload');
    this.router.navigate(['/upload']);
  }
}
