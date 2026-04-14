import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigurationGedComponent } from './configuration-ged.component';

describe('ConfigurationGedComponent', () => {
  let component: ConfigurationGedComponent;
  let fixture: ComponentFixture<ConfigurationGedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfigurationGedComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConfigurationGedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
