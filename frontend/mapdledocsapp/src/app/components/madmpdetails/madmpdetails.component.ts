import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {SearchService} from '../../service/search.service';
import {MaDmpDto} from "../../dto/madmp-dto";
import {saveAs} from "file-saver";

@Component({
  selector: 'app-madmpdetails',
  templateUrl: './madmpdetails.component.html',
  styleUrls: ['./madmpdetails.component.scss']
})
export class MadmpdetailsComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private searchService: SearchService) {
  }

  maDmp: MaDmpDto;
  json: string = null;
  objKeys: string[];

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.searchService.getMaDmp(id)
      .subscribe(maDmp => {
        this.maDmp = new MaDmpDto(maDmp.json, maDmp.docId, maDmp.userId, maDmp.fieldsToHide);
        this.json = JSON.parse(maDmp.json);
        delete this.json.fulltextString;
      });
  }

  onDownload() {
    const blob = new Blob([JSON.stringify(this.json)], {type: 'text'});
    saveAs(blob, 'madmp.json');
  }
}
