import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateConnectorComponent } from './update-connector.component';

describe('UpdateConnectorComponent', () => {
  let component: UpdateConnectorComponent;
  let fixture: ComponentFixture<UpdateConnectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateConnectorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdateConnectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
