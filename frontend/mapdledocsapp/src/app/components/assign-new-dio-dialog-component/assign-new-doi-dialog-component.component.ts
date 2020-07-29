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

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AssignNewDoiDialogComponentComponent>) {
  }


  ngOnInit() {
    this.form = this.fb.group({});
  }

  yes() {
    this.dialogRef.close('yes');
  }

  cancel() {
    this.dialogRef.close(null);
  }
}
