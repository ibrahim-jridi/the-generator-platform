import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AllFileMangementComponent } from './all-file-mangement.component';

describe('AllFileMangementComponent', () => {
  let component: AllFileMangementComponent;
  let fixture: ComponentFixture<AllFileMangementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AllFileMangementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AllFileMangementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
