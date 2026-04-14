import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BsConfigurationComponent } from './bs-configuration.component';

describe('BsConfigurationComponent', () => {
  let component: BsConfigurationComponent;
  let fixture: ComponentFixture<BsConfigurationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BsConfigurationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BsConfigurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
