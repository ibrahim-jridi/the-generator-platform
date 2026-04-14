import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateConfigurationGedComponent } from './update-configuration-ged.component';

describe('UpdateConfigurationGedComponent', () => {
  let component: UpdateConfigurationGedComponent;
  let fixture: ComponentFixture<UpdateConfigurationGedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateConfigurationGedComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdateConfigurationGedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
