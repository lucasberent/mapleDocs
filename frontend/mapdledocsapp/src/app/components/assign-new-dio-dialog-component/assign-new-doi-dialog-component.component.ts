import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-assign-new-dio-dialog-component',
  templateUrl: './assign-new-doi-dialog-component.component.html',
  styleUrls: ['./assign-new-doi-dialog-component.component.css']
})
export class AssignNewDoiDialogComponentComponent implements OnInit {

  form: FormGroup;
  description: string;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AssignNewDoiDialogComponentComponent>) {
  }


  ngOnInit() {

  }

  yes() {
    this.dialogRef.close(true);
  }

  no() {
    this.dialogRef.close(false);
  }

  cancel() {
    this.dialogRef.close(null);
  }
}
