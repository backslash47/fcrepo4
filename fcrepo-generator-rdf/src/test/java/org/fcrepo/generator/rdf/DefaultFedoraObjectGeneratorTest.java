package org.fcrepo.generator.rdf;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.ws.rs.core.UriInfo;

import org.fcrepo.FedoraObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DefaultNodeGenerator.class})
public class DefaultFedoraObjectGeneratorTest {

	private DefaultFedoraObjectGenerator testObj;
	
	@Before
	public void setUp() {
		testObj = new DefaultFedoraObjectGenerator();
	}
	
	@Test
	public void testGetTriples() throws RepositoryException {
        String path = "/testing/fake/object";
        UriInfo mockUris = mock(UriInfo.class);
        Node mockNode = mock(Node.class);
        when(mockNode.getPath()).thenReturn(path);
		FedoraObject mockObj = mock(FedoraObject.class);
		when(mockObj.getNode()).thenReturn(mockNode);
		mockStatic(DefaultNodeGenerator.class);
		when(DefaultNodeGenerator.getTriples(mockNode, mockUris))
		.thenReturn(null);
		testObj.getTriples(mockObj, mockUris);
		verify(mockObj).getNode();
		verifyStatic();
		DefaultNodeGenerator.getTriples(mockNode, mockUris);
	}
}
