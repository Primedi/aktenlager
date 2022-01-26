import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AkteService } from '../service/akte.service';
import { IAkte, Akte } from '../akte.model';
import { IRaum } from 'app/entities/raum/raum.model';
import { RaumService } from 'app/entities/raum/service/raum.service';

import { AkteUpdateComponent } from './akte-update.component';

describe('Akte Management Update Component', () => {
  let comp: AkteUpdateComponent;
  let fixture: ComponentFixture<AkteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let akteService: AkteService;
  let raumService: RaumService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AkteUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AkteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AkteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    akteService = TestBed.inject(AkteService);
    raumService = TestBed.inject(RaumService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Raum query and add missing value', () => {
      const akte: IAkte = { id: 456 };
      const raum: IRaum = { id: 52319 };
      akte.raum = raum;

      const raumCollection: IRaum[] = [{ id: 77994 }];
      jest.spyOn(raumService, 'query').mockReturnValue(of(new HttpResponse({ body: raumCollection })));
      const additionalRaums = [raum];
      const expectedCollection: IRaum[] = [...additionalRaums, ...raumCollection];
      jest.spyOn(raumService, 'addRaumToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ akte });
      comp.ngOnInit();

      expect(raumService.query).toHaveBeenCalled();
      expect(raumService.addRaumToCollectionIfMissing).toHaveBeenCalledWith(raumCollection, ...additionalRaums);
      expect(comp.raumsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const akte: IAkte = { id: 456 };
      const raum: IRaum = { id: 14502 };
      akte.raum = raum;

      activatedRoute.data = of({ akte });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(akte));
      expect(comp.raumsSharedCollection).toContain(raum);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Akte>>();
      const akte = { id: 123 };
      jest.spyOn(akteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ akte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: akte }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(akteService.update).toHaveBeenCalledWith(akte);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Akte>>();
      const akte = new Akte();
      jest.spyOn(akteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ akte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: akte }));
      saveSubject.complete();

      // THEN
      expect(akteService.create).toHaveBeenCalledWith(akte);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Akte>>();
      const akte = { id: 123 };
      jest.spyOn(akteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ akte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(akteService.update).toHaveBeenCalledWith(akte);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRaumById', () => {
      it('Should return tracked Raum primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRaumById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
