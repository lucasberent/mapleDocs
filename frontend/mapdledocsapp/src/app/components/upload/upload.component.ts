import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {Ajv} from 'ajv';
import {UploadService} from '../../service/upload.service';
import {HttpClient} from '@angular/common/http';
import {CreateMaDmpDto} from '../../dto/create-madmp-dto';

// @ts-ignore
const Ajv = require('ajv');

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {
  maDmpJsonSchema: any;
  ajv: Ajv = null;
  madmpToCreate: CreateMaDmpDto;
  DETAILS_ROUTE = '/details';
  @ViewChild('fileInput') fileInput: ElementRef;

  constructor(private uploadService: UploadService,
              private httpClient: HttpClient,
              private router: Router) {
  }

  ngOnInit(): void {
    this.ajv = new Ajv();
    this.maDmpJsonSchema = this.httpClient.get('assets/schemas/schema-version-1.0.json')
      .subscribe(file => console.log('read schema: ' + file));
  }

  handleFileInput(files) {
    const fileToUpload = files.item(0);
    if (!fileToUpload || !files) {
      console.log('no file selected')
      this.handleAbortUpload();
    }
    if (!fileToUpload.name.endsWith('.json')) {
      alert('only json files accepted');
      this.handleAbortUpload();
      return;
    }
    fileToUpload.text().then(contents => {
      const valid = this.ajv.validate(this.maDmpJsonSchema, contents);
      if (!valid) {
        alert('maDmp not valid according to schema version 1.0');
        this.handleAbortUpload();
      } else {
        console.log('maDmp validated successfully against schema version 1.0');
        this.madmpToCreate = new CreateMaDmpDto(contents);
      }
    });
  }

  handleUpload() {
    this.upload(this.madmpToCreate);
  }

  handleAbortUpload() {
    this.madmpToCreate = null;
  }

  upload(createMaDmpDto: CreateMaDmpDto) {
    if (!createMaDmpDto) {
      console.log('no file chosen');
      return;
    }
    this.uploadService.uploadMaDmp(createMaDmpDto)
      .subscribe(dmpId => {
        this.router.navigate([this.DETAILS_ROUTE + '/' + dmpId]);
      });
  }

  onClearSelection() {
    this.fileInput.nativeElement.value = '';
    this.handleAbortUpload();
  }
}
