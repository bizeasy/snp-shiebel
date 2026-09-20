import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('FacilityGroupType e2e test', () => {
  const facilityGroupTypePageUrl = '/facility-group-type';
  let username: string;
  let password: string;
  const facilityGroupTypeSample = { name: 'excluding' };

  let facilityGroupType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/facility-group-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/facility-group-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/facility-group-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (facilityGroupType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/facility-group-types/${facilityGroupType.id}`,
      }).then(() => {
        facilityGroupType = undefined;
      });
    }
  });

  it('FacilityGroupTypes menu should load FacilityGroupTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('facility-group-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FacilityGroupType').should('exist');
    cy.location('pathname').should('eq', facilityGroupTypePageUrl);
  });

  describe('FacilityGroupType page', () => {
    it('should have translated page title', () => {
      cy.visit(facilityGroupTypePageUrl);
      cy.getEntityHeading('FacilityGroupType').should('not.contain', 'snpApp.facilityGroupType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(facilityGroupTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FacilityGroupType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${facilityGroupTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FacilityGroupType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/facility-group-types',
          body: facilityGroupTypeSample,
        }).then(({ body }) => {
          facilityGroupType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/facility-group-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [facilityGroupType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(facilityGroupTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FacilityGroupType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('facilityGroupType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupTypePageUrl);
      });

      it('edit button click should load edit FacilityGroupType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityGroupType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupTypePageUrl);
      });

      it('edit button click should load edit FacilityGroupType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FacilityGroupType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupTypePageUrl);
      });

      it('last delete button click should delete instance of FacilityGroupType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('facilityGroupType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', facilityGroupTypePageUrl);

        facilityGroupType = undefined;
      });
    });
  });

  describe('new FacilityGroupType page', () => {
    beforeEach(() => {
      cy.visit(facilityGroupTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FacilityGroupType');
    });

    it('should create an instance of FacilityGroupType', () => {
      cy.get(`[data-cy="name"]`).type('ha');
      cy.get(`[data-cy="name"]`).should('have.value', 'ha');

      cy.get(`[data-cy="description"]`).type('short');
      cy.get(`[data-cy="description"]`).should('have.value', 'short');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        facilityGroupType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', facilityGroupTypePageUrl);
    });
  });
});
