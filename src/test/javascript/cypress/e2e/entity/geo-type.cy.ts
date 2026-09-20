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

describe('GeoType e2e test', () => {
  const geoTypePageUrl = '/geo-type';
  let username: string;
  let password: string;
  const geoTypeSample = { name: 'furthermore officially' };

  let geoType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/geo-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/geo-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/geo-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (geoType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/geo-types/${geoType.id}`,
      }).then(() => {
        geoType = undefined;
      });
    }
  });

  it('GeoTypes menu should load GeoTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('geo-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GeoType').should('exist');
    cy.location('pathname').should('eq', geoTypePageUrl);
  });

  describe('GeoType page', () => {
    it('should have translated page title', () => {
      cy.visit(geoTypePageUrl);
      cy.getEntityHeading('GeoType').should('not.contain', 'snpApp.geoType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(geoTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GeoType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${geoTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('GeoType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/geo-types',
          body: geoTypeSample,
        }).then(({ body }) => {
          geoType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/geo-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [geoType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(geoTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GeoType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('geoType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoTypePageUrl);
      });

      it('edit button click should load edit GeoType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GeoType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoTypePageUrl);
      });

      it('edit button click should load edit GeoType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GeoType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoTypePageUrl);
      });

      it('last delete button click should delete instance of GeoType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('geoType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoTypePageUrl);

        geoType = undefined;
      });
    });
  });

  describe('new GeoType page', () => {
    beforeEach(() => {
      cy.visit(geoTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('GeoType');
    });

    it('should create an instance of GeoType', () => {
      cy.get(`[data-cy="name"]`).type('inasmuch');
      cy.get(`[data-cy="name"]`).should('have.value', 'inasmuch');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        geoType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', geoTypePageUrl);
    });
  });
});
