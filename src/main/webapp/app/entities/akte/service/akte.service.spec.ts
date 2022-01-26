import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAkte, Akte } from '../akte.model';

import { AkteService } from './akte.service';

describe('Akte Service', () => {
  let service: AkteService;
  let httpMock: HttpTestingController;
  let elemDefault: IAkte;
  let expectedResult: IAkte | IAkte[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AkteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      aktenthema: 'AAAAAAA',
      organisationsEinheit: 'AAAAAAA',
      aktenMeter: 0,
      haengend: false,
      standort: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Akte', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Akte()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Akte', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          aktenthema: 'BBBBBB',
          organisationsEinheit: 'BBBBBB',
          aktenMeter: 1,
          haengend: true,
          standort: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Akte', () => {
      const patchObject = Object.assign(
        {
          aktenthema: 'BBBBBB',
          organisationsEinheit: 'BBBBBB',
        },
        new Akte()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Akte', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          aktenthema: 'BBBBBB',
          organisationsEinheit: 'BBBBBB',
          aktenMeter: 1,
          haengend: true,
          standort: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Akte', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAkteToCollectionIfMissing', () => {
      it('should add a Akte to an empty array', () => {
        const akte: IAkte = { id: 123 };
        expectedResult = service.addAkteToCollectionIfMissing([], akte);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(akte);
      });

      it('should not add a Akte to an array that contains it', () => {
        const akte: IAkte = { id: 123 };
        const akteCollection: IAkte[] = [
          {
            ...akte,
          },
          { id: 456 },
        ];
        expectedResult = service.addAkteToCollectionIfMissing(akteCollection, akte);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Akte to an array that doesn't contain it", () => {
        const akte: IAkte = { id: 123 };
        const akteCollection: IAkte[] = [{ id: 456 }];
        expectedResult = service.addAkteToCollectionIfMissing(akteCollection, akte);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(akte);
      });

      it('should add only unique Akte to an array', () => {
        const akteArray: IAkte[] = [{ id: 123 }, { id: 456 }, { id: 68490 }];
        const akteCollection: IAkte[] = [{ id: 123 }];
        expectedResult = service.addAkteToCollectionIfMissing(akteCollection, ...akteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const akte: IAkte = { id: 123 };
        const akte2: IAkte = { id: 456 };
        expectedResult = service.addAkteToCollectionIfMissing([], akte, akte2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(akte);
        expect(expectedResult).toContain(akte2);
      });

      it('should accept null and undefined values', () => {
        const akte: IAkte = { id: 123 };
        expectedResult = service.addAkteToCollectionIfMissing([], null, akte, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(akte);
      });

      it('should return initial array if no Akte is added', () => {
        const akteCollection: IAkte[] = [{ id: 123 }];
        expectedResult = service.addAkteToCollectionIfMissing(akteCollection, undefined, null);
        expect(expectedResult).toEqual(akteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
