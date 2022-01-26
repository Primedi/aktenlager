import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AkteDetailComponent } from './akte-detail.component';

describe('Akte Management Detail Component', () => {
  let comp: AkteDetailComponent;
  let fixture: ComponentFixture<AkteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AkteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ akte: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AkteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AkteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load akte on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.akte).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
