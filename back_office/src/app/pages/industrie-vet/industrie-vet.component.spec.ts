import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndustrieVetComponent } from './industrie-vet.component';

describe('IndustrieVetComponent', () => {
  let component: IndustrieVetComponent;
  let fixture: ComponentFixture<IndustrieVetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndustrieVetComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(IndustrieVetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
