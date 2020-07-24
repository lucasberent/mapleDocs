import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {Ajv} from 'ajv';
import {UploadService} from '../../service/upload.service';
import {HttpClient} from '@angular/common/http';
import {CreateMaDmpDto} from '../../dto/create-madmp-dto';
import {ToastrService} from 'ngx-toastr';
import {FormControl} from '@angular/forms';
import {AssignNewDoiDialogComponentComponent} from '../assign-new-dio-dialog-component/assign-new-doi-dialog-component.component';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';

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
  assignNewDoi: boolean = false;
  doiServicePassword: string;

  constructor(private uploadService: UploadService,
              private httpClient: HttpClient,
              private router: Router,
              private toastr: ToastrService,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.ajv = new Ajv();
    this.maDmpJsonSchema = this.httpClient.get('assets/schemas/schema-version-1.0.json')
      .subscribe(file => console.log('read schema: ' + file));
  }

  handleFileInput(files) {
    const fileToUpload = files.item(0);
    if (!fileToUpload || !files) {
      console.log('no file selected');
      this.resetInputs();
      return;
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
        this.madmpToCreate = new CreateMaDmpDto(contents, [], this.assignNewDoi, this.doiServicePassword);
        this.madmpShowJson = JSON.parse(this.madmpToCreate.json);
        this.madmpFields = Object.keys(this.madmpShowJson['dmp']);
      }
    });
  }

  handleUpload() {
    if (!this.madmpShowJson) {
      this.toastr.info('no maDMP selected');
      this.resetInputs();
      return;
    }
    if (!this.madmpShowJson['dmp'].hasOwnProperty('dmp_id')) {
      this.handleDialogInput();
    } else {
      this.uploadMaDmpToCreate();
    }
  }

  handleDialogInput() {
    this.openDialog().subscribe(data => {
      console.log('Dialog decision:', data);
      if (!data) {
        this.toastr.info('Upload cancelled');
        return;
      } else {
        this.madmpToCreate.assignNewDoi = true;
        this.madmpToCreate.doiServicePassword = data;
        this.uploadMaDmpToCreate();
      }
    });
  }

  openDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    const dialogRef = this.dialog.open(AssignNewDoiDialogComponentComponent, dialogConfig);

    return dialogRef.afterClosed();
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
      this.selectedFieldsToHide = fieldNames;
      fieldNames.forEach((field) => {
        delete this.madmpShowJson['dmp'][field];
      });
      console.log('added ' + fieldNames + ' to fields to hide')
    }
  }

  uploadMaDmpToCreate() {
    if (!this.madmpToCreate) {
      console.log('no file chosen');
      this.toastr.error('no file chosen, select .json to upload');
      this.resetInputs();
      return;
    }
    this.uploading = true;
    this.madmpToCreate.fieldsToHide = this.selectedFieldsToHide;
    console.log('uploading madmp:');
    console.log(this.madmpToCreate);
    this.uploadService.uploadMaDmp(this.madmpToCreate)
      .subscribe(result => {
        this.uploading = false;
        this.toastr.success('upload successful');
        this.resetInputs();
      }, error => {
        this.toastr.error('error uploading maDMP (maybe a problem with assigning a new doi at the doi service');
        console.log(error);
      });
  }

  onClearSelection() {
    this.resetInputs();
    this.toastr.info('Cleared selection');
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
