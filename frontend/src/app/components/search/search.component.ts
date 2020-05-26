import {Component, OnInit} from '@angular/core';
import {SearchService} from '../../service/search.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  madmps: any[];

  constructor(private searchService: SearchService) {
  }

  ngOnInit(): void {
    this.searchService.findMaDmps('').subscribe(madmps => this.madmps = madmps);
  }

  onSelect(madmp) {
    // TODO show madmp details
  }

  onEnter(value: string) {
    this.doSearch(value);
  }

  doSearch(value: string) {
    console.log('searching for: ' + value);
  }
}
