import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRaum, Raum } from '../raum.model';

import { RaumService } from './raum.service';

describe('Raum Service', () => {
  let service: RaumService;
  let httpMock: HttpTestingController;
  let elemDefault: IRaum;
  let expectedResult: IRaum | IRaum[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RaumService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      gebaeude: 'AAAAAAA',
      raumnummer: 0,
      etage: 0,
      zusatz: 'AAAAAAA',
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

    it('should create a Raum', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Raum()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Raum', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          gebaeude: 'BBBBBB',
          raumnummer: 1,
          etage: 1,
          zusatz: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Raum', () => {
      const patchObject = Object.assign(
        {
          gebaeude: 'BBBBBB',
          etage: 1,
        },
        new Raum()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Raum', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          gebaeude: 'BBBBBB',
          raumnummer: 1,
          etage: 1,
          zusatz: 'BBBBBB',
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

    it('should delete a Raum', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRaumToCollectionIfMissing', () => {
      it('should add a Raum to an empty array', () => {
        const raum: IRaum = { id: 123 };
        expectedResult = service.addRaumToCollectionIfMissing([], raum);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(raum);
      });

      it('should not add a Raum to an array that contains it', () => {
        const raum: IRaum = { id: 123 };
        const raumCollection: IRaum[] = [
          {
            ...raum,
          },
          { id: 456 },
        ];
        expectedResult = service.addRaumToCollectionIfMissing(raumCollection, raum);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Raum to an array that doesn't contain it", () => {
        const raum: IRaum = { id: 123 };
        const raumCollection: IRaum[] = [{ id: 456 }];
        expectedResult = service.addRaumToCollectionIfMissing(raumCollection, raum);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(raum);
      });

      it('should add only unique Raum to an array', () => {
        const raumArray: IRaum[] = [{ id: 123 }, { id: 456 }, { id: 93772 }];
        const raumCollection: IRaum[] = [{ id: 123 }];
        expectedResult = service.addRaumToCollectionIfMissing(raumCollection, ...raumArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const raum: IRaum = { id: 123 };
        const raum2: IRaum = { id: 456 };
        expectedResult = service.addRaumToCollectionIfMissing([], raum, raum2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(raum);
        expect(expectedResult).toContain(raum2);
      });

      it('should accept null and undefined values', () => {
        const raum: IRaum = { id: 123 };
        expectedResult = service.addRaumToCollectionIfMissing([], null, raum, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(raum);
      });

      it('should return initial array if no Raum is added', () => {
        const raumCollection: IRaum[] = [{ id: 123 }];
        expectedResult = service.addRaumToCollectionIfMissing(raumCollection, undefined, null);
        expect(expectedResult).toEqual(raumCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
