import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddExtensionComponent } from './add-extension.component';

describe('AddRoleComponent', () => {
  let component: AddExtensionComponent;
  let fixture: ComponentFixture<AddExtensionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddExtensionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddExtensionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
