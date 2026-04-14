import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyFileMangementComponent } from './my-file-mangement.component';

describe('MyFileMangementComponent', () => {
  let component: MyFileMangementComponent;
  let fixture: ComponentFixture<MyFileMangementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyFileMangementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyFileMangementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
