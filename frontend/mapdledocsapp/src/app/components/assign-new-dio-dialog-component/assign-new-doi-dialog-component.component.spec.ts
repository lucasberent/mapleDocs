import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignNewDoiDialogComponentComponent } from './assign-new-doi-dialog-component.component';

describe('AssignNewDioDialogComponentComponent', () => {
  let component: AssignNewDoiDialogComponentComponent;
  let fixture: ComponentFixture<AssignNewDoiDialogComponentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AssignNewDoiDialogComponentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignNewDoiDialogComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
