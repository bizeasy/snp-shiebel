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

describe('LocationType e2e test', () => {
  const locationTypePageUrl = '/location-type';
  let username: string;
  let password: string;
  const locationTypeSample = { name: 'guilt' };

  let locationType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/location-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/location-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/location-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (locationType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/location-types/${locationType.id}`,
      }).then(() => {
        locationType = undefined;
      });
    }
  });

  it('LocationTypes menu should load LocationTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('location-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LocationType').should('exist');
    cy.location('pathname').should('eq', locationTypePageUrl);
  });

  describe('LocationType page', () => {
    it('should have translated page title', () => {
      cy.visit(locationTypePageUrl);
      cy.getEntityHeading('LocationType').should('not.contain', 'snpApp.locationType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(locationTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LocationType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${locationTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('LocationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', locationTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/location-types',
          body: locationTypeSample,
        }).then(({ body }) => {
          locationType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/location-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [locationType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(locationTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LocationType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('locationType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', locationTypePageUrl);
      });

      it('edit button click should load edit LocationType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LocationType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', locationTypePageUrl);
      });

      it('edit button click should load edit LocationType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LocationType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', locationTypePageUrl);
      });

      it('last delete button click should delete instance of LocationType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('locationType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', locationTypePageUrl);

        locationType = undefined;
      });
    });
  });

  describe('new LocationType page', () => {
    beforeEach(() => {
      cy.visit(locationTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LocationType');
    });

    it('should create an instance of LocationType', () => {
      cy.get(`[data-cy="name"]`).type('mozzarella humiliating');
      cy.get(`[data-cy="name"]`).should('have.value', 'mozzarella humiliating');

      cy.get(`[data-cy="description"]`).type('brr modulo pretty');
      cy.get(`[data-cy="description"]`).should('have.value', 'brr modulo pretty');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        locationType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', locationTypePageUrl);
    });
  });
});
