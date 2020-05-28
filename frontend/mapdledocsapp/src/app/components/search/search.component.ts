import {Component, OnInit} from '@angular/core';
import {SearchService} from '../../service/search.service';
import {Router} from "@angular/router";
import {MaDmpDto} from "../../dto/madmp-dto";
import {MaDmpDisplayObject} from "../../dto/madmp-display-object";
import {newArray} from "@angular/compiler/src/util";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  madmpDisplayList: MaDmpDisplayObject[] = [];

  constructor(private searchService: SearchService, private router: Router) {
  }

  ngOnInit(): void {
    this.searchService.findMaDmps('', 1, 10).subscribe(madmps => this.setDisplayMadmps(madmps)); // TODO pagination
  }

  private setDisplayMadmps(madmps: MaDmpDto[]) {
    this.madmpDisplayList = [];
    madmps.forEach(madmp => {
      const json = JSON.parse(madmp.json);
      const docId = madmp.docId;
      console.log('json: ');
      console.log(json);
      console.log('docId:');
      console.log(docId);
      const madmpDisplay = new MaDmpDisplayObject(json, docId);
      console.log('displayobj:')
      console.log(madmpDisplay);
      this.madmpDisplayList.push(madmpDisplay);
    });
  }

  onEnter(value: string) {
    this.doSearch(value);
  }

  doSearch(value: string) {
    console.log('searching for: ' + value);
    this.searchService.findMaDmps(value, 1, 10).subscribe(madmps => this.setDisplayMadmps(madmps));
  }

  onToUpload() {
    console.log('to upload');
    this.router.navigate(['/upload']);
  }
}