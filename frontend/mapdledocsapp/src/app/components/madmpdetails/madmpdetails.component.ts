import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {SearchService} from '../../service/search.service';
import {MaDmpDto} from "../../dto/madmp-dto";
import {saveAs, saveAs as importedSaveAs} from "file-saver";

@Component({
  selector: 'app-madmpdetails',
  templateUrl: './madmpdetails.component.html',
  styleUrls: ['./madmpdetails.component.css']
})
export class MadmpdetailsComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private searchService: SearchService) {
  }

  maDmp: MaDmpDto;
  json: string = null;
  objKeys:string[];

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    console.log("getting madmp for id");
    console.log(id);
    this.searchService.getMaDmp(id)
      .subscribe(maDmp => {
        console.log('got: ')
        console.log(maDmp);
        this.maDmp = new MaDmpDto(maDmp.json, maDmp.docId, maDmp.userId, maDmp.fieldsToHide);
        console.log('this');
        console.log(this.maDmp);
        console.log('json:')
        this.json = JSON.parse(maDmp.json)
        console.log(this.json);
      });
  }

  onDownload() {
    const blob = new Blob([JSON.stringify(this.json)], { type: 'text' });
    saveAs(blob, 'madmp.json');
  }
}
