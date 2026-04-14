import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormManagementViewComponent } from './form-management-view.component';

describe('FormManagementViewComponent', () => {
  let component: FormManagementViewComponent;
  let fixture: ComponentFixture<FormManagementViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormManagementViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormManagementViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
