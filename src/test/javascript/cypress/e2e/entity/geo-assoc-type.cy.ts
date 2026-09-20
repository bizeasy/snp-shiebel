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

describe('GeoAssocType e2e test', () => {
  const geoAssocTypePageUrl = '/geo-assoc-type';
  let username: string;
  let password: string;
  const geoAssocTypeSample = { name: 'accompanist boastfully qu' };

  let geoAssocType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/geo-assoc-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/geo-assoc-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/geo-assoc-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (geoAssocType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/geo-assoc-types/${geoAssocType.id}`,
      }).then(() => {
        geoAssocType = undefined;
      });
    }
  });

  it('GeoAssocTypes menu should load GeoAssocTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('geo-assoc-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GeoAssocType').should('exist');
    cy.location('pathname').should('eq', geoAssocTypePageUrl);
  });

  describe('GeoAssocType page', () => {
    it('should have translated page title', () => {
      cy.visit(geoAssocTypePageUrl);
      cy.getEntityHeading('GeoAssocType').should('not.contain', 'snpApp.geoAssocType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(geoAssocTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GeoAssocType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${geoAssocTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('GeoAssocType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoAssocTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/geo-assoc-types',
          body: geoAssocTypeSample,
        }).then(({ body }) => {
          geoAssocType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/geo-assoc-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [geoAssocType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(geoAssocTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GeoAssocType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('geoAssocType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoAssocTypePageUrl);
      });

      it('edit button click should load edit GeoAssocType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GeoAssocType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoAssocTypePageUrl);
      });

      it('edit button click should load edit GeoAssocType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GeoAssocType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoAssocTypePageUrl);
      });

      it('last delete button click should delete instance of GeoAssocType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('geoAssocType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoAssocTypePageUrl);

        geoAssocType = undefined;
      });
    });
  });

  describe('new GeoAssocType page', () => {
    beforeEach(() => {
      cy.visit(geoAssocTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('GeoAssocType');
    });

    it('should create an instance of GeoAssocType', () => {
      cy.get(`[data-cy="name"]`).type('reproachfully after chain');
      cy.get(`[data-cy="name"]`).should('have.value', 'reproachfully after chain');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        geoAssocType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', geoAssocTypePageUrl);
    });
  });
});
