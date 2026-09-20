import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IGeo } from 'app/entities/geo/geo.model';
import { GeoService } from 'app/entities/geo/service/geo.service';
import { TaxAuthorityService } from '../service/tax-authority.service';
import { ITaxAuthority } from '../tax-authority.model';

import { TaxAuthorityFormService } from './tax-authority-form.service';
import { TaxAuthorityUpdate } from './tax-authority-update';

describe('TaxAuthority Management Update Component', () => {
  let comp: TaxAuthorityUpdate;
  let fixture: ComponentFixture<TaxAuthorityUpdate>;
  let activatedRoute: ActivatedRoute;
  let taxAuthorityFormService: TaxAuthorityFormService;
  let taxAuthorityService: TaxAuthorityService;
  let geoService: GeoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(TaxAuthorityUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    taxAuthorityFormService = TestBed.inject(TaxAuthorityFormService);
    taxAuthorityService = TestBed.inject(TaxAuthorityService);
    geoService = TestBed.inject(GeoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Geo query and add missing value', () => {
      const taxAuthority: ITaxAuthority = { id: 27648 };
      const geo: IGeo = { id: 16292 };
      taxAuthority.geo = geo;

      const geoCollection: IGeo[] = [{ id: 16292 }];
      vi.spyOn(geoService, 'query').mockReturnValue(of(new HttpResponse({ body: geoCollection })));
      const additionalGeos = [geo];
      const expectedCollection: IGeo[] = [...additionalGeos, ...geoCollection];
      vi.spyOn(geoService, 'addGeoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ taxAuthority });
      comp.ngOnInit();

      expect(geoService.query).toHaveBeenCalled();
      expect(geoService.addGeoToCollectionIfMissing).toHaveBeenCalledWith(
        geoCollection,
        ...additionalGeos.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.geosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const taxAuthority: ITaxAuthority = { id: 27648 };
      const geo: IGeo = { id: 16292 };
      taxAuthority.geo = geo;

      activatedRoute.data = of({ taxAuthority });
      comp.ngOnInit();

      expect(comp.geosSharedCollection()).toContainEqual(geo);
      expect(comp.taxAuthority).toEqual(taxAuthority);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITaxAuthority>();
      const taxAuthority = { id: 14408 };
      vi.spyOn(taxAuthorityFormService, 'getTaxAuthority').mockReturnValue(taxAuthority);
      vi.spyOn(taxAuthorityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxAuthority });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(taxAuthority);
      saveSubject.complete();

      // THEN
      expect(taxAuthorityFormService.getTaxAuthority).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(taxAuthorityService.update).toHaveBeenCalledWith(expect.objectContaining(taxAuthority));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITaxAuthority>();
      const taxAuthority = { id: 14408 };
      vi.spyOn(taxAuthorityFormService, 'getTaxAuthority').mockReturnValue({ id: null });
      vi.spyOn(taxAuthorityService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxAuthority: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(taxAuthority);
      saveSubject.complete();

      // THEN
      expect(taxAuthorityFormService.getTaxAuthority).toHaveBeenCalled();
      expect(taxAuthorityService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITaxAuthority>();
      const taxAuthority = { id: 14408 };
      vi.spyOn(taxAuthorityService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ taxAuthority });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(taxAuthorityService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGeo', () => {
      it('should forward to geoService', () => {
        const entity = { id: 16292 };
        const entity2 = { id: 23406 };
        vi.spyOn(geoService, 'compareGeo');
        comp.compareGeo(entity, entity2);
        expect(geoService.compareGeo).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
