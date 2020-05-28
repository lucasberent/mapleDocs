import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MadmpdetailsComponent } from './madmpdetails.component';

describe('MadmpdetailsComponent', () => {
  let component: MadmpdetailsComponent;
  let fixture: ComponentFixture<MadmpdetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MadmpdetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MadmpdetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
