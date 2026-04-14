import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndustrieHumainComponent } from './industrie-humain.component';

describe('IndustrieHumainComponent', () => {
  let component: IndustrieHumainComponent;
  let fixture: ComponentFixture<IndustrieHumainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndustrieHumainComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(IndustrieHumainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
