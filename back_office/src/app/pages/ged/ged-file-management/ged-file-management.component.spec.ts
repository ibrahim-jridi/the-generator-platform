import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GedFileManagementComponent } from './ged-file-management.component';

describe('GedFileManagementComponent', () => {
  let component: GedFileManagementComponent;
  let fixture: ComponentFixture<GedFileManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GedFileManagementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GedFileManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
