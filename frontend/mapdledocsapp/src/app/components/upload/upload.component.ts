import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {Ajv} from 'ajv';
import {UploadService} from '../../service/upload.service';
import {HttpClient} from '@angular/common/http';
import {CreateMaDmpDto} from '../../dto/create-madmp-dto';
import {ToastrService} from 'ngx-toastr';
import {FormControl} from "@angular/forms";
import {getMatIconFailedToSanitizeLiteralError} from "@angular/material/icon";
import {TreeviewItem} from "ngx-treeview";

// @ts-ignore
const Ajv = require('ajv');

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss']
})
export class UploadComponent implements OnInit {
  fieldsToHideFormControl = new FormControl();
  maDmpJsonSchema: any;
  ajv: Ajv = null;
  madmpToCreate: CreateMaDmpDto;
  madmpShowJson: any;
  DETAILS_ROUTE = '/details';
  @ViewChild('fileInput') fileInput: ElementRef;
  uploading: boolean = false;
  madmpFields: any[];
  selectedFieldsToHide: string[] = [];

  constructor(private uploadService: UploadService,
              private httpClient: HttpClient,
              private router: Router,
              private toastr: ToastrService) {
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
      this.resetInputs();
    }
    if (!fileToUpload.name.endsWith('.json')) {
      this.toastr.error('only json files accepted');
      this.resetInputs();
      return;
    }
    fileToUpload.text().then(contents => {
      const valid = this.ajv.validate(this.maDmpJsonSchema, contents);
      if (!valid) {
        this.toastr.error('maDmp not valid according to schema version 1.0');
        this.resetInputs();
      } else {
        console.log('maDmp validated successfully against schema version 1.0');
        this.madmpToCreate = new CreateMaDmpDto(contents, []);
        this.madmpShowJson = JSON.parse(this.madmpToCreate.json);
        this.madmpFields = Object.keys(this.madmpShowJson['dmp']);
      }
    });
  }

  handleUpload() {
    this.uploading = true;
    this.madmpToCreate.fieldsToHide = this.selectedFieldsToHide;
    this.upload(this.madmpToCreate);
    this.uploading = false;
    this.resetInputs();
  }

  resetInputs() {
    this.madmpToCreate = null;
    this.madmpShowJson = null;
    this.madmpFields = null;
    this.fileInput.nativeElement.value = '';
    this.resetFieldsToHide();
  }

  handleUpdateFieldsToHide(fieldNames: string[]) {
    if (fieldNames) {
      this.selectedFieldsToHide = fieldNames
      fieldNames.forEach((field) => {
        delete this.madmpShowJson['dmp'][field];
      });
      console.log('added ' + fieldNames + ' to fields to hide')
    }
  }

  upload(createMaDmpDto: CreateMaDmpDto) {
    if (!createMaDmpDto) {
      console.log('no file chosen');
      this.toastr.error('no file chosen, select .json to upload');
      return;
    }
    this.uploadService.uploadMaDmp(createMaDmpDto)
      .subscribe(dmpId => {
        this.toastr.success('upload successful')
      });
  }

  onClearSelection() {
    this.resetInputs();
  }

  isUploading() {
    return this.uploading;
  }

  resetFieldsToHide() {
    this.fieldsToHideFormControl = new FormControl();
    this.selectedFieldsToHide = [];
  }

  onClearHideFieldsSelection() {
    this.resetFieldsToHide();
  }
}
